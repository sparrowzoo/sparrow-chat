package com.sparrow.chat.contact.test;

import com.sparrow.datasource.DataSourceFactoryImpl;
import com.sparrow.spring.starter.SparrowDataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassReaderTest {
    public static void main(String[] args) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(SparrowDataSourceAutoConfiguration.class.getName());
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        for (MethodMetadata methodMetadata : annotationMetadata.getAnnotatedMethods(Bean.class.getName())) {
            System.out.println(methodMetadata.getMethodName());
            if (methodMetadata.getMethodName().equalsIgnoreCase("dataSourceFactory")) {
                System.out.println(methodMetadata.getReturnTypeName());
                SparrowDataSourceAutoConfiguration configuration = new SparrowDataSourceAutoConfiguration();
                Method method = SparrowDataSourceAutoConfiguration.class.getMethod("dataSourceFactory");
                DataSourceFactoryImpl factory = (DataSourceFactoryImpl) method.invoke(configuration);
                System.out.println(factory);
            }
        }
        Resource resource = metadataReader.getResource();
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
    }
}
