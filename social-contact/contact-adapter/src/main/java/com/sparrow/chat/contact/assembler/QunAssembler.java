package com.sparrow.chat.contact.assembler;

import com.sparrow.chat.contact.bo.*;
import com.sparrow.chat.contact.protocol.enums.Category;
import com.sparrow.chat.contact.protocol.enums.ContactError;
import com.sparrow.chat.contact.protocol.enums.Nationality;
import com.sparrow.chat.contact.protocol.vo.*;
import com.sparrow.exception.Asserts;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.utility.BeanUtility;
import com.sparrow.utility.CollectionsUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import java.util.*;

@Named
public class QunAssembler {
    private static Logger logger = LoggerFactory.getLogger(QunAssembler.class);
    @Autowired
    private UserAssembler userAssembler;

    public QunVO assembleQun(QunDetailWrapBO qunDetail) throws BusinessException {
        QunBO qunBO = qunDetail.getQun();
        QunVO qunVo = new QunVO();
        ContactVO owner =this.userAssembler.userDto2ContactVo(qunDetail.getOwner());
        this.assembleQun(qunBO, owner);
        return qunVo;
    }

    public QunVO assembleQun(QunBO qunBO, ContactVO userProfile) throws BusinessException {
        QunVO qunVo = new QunVO();
        BeanUtility.copyProperties(qunBO, qunVo);
        qunVo.setQunId(qunBO.getId().toString());
        qunVo.setQunName(qunBO.getName());
        Nationality nationality = Nationality.getById(qunBO.getNationalityId());
        Asserts.isTrue(nationality == null, ContactError.NATIONALITY_OF_QUN_EMPTY);
        qunVo.setNationality(nationality.getName());
        if (userProfile != null) {
            qunVo.setOwnerName(userProfile.getUserName());
        }
        //todo
        Category category = Category.getById(qunBO.getCategoryId());
        Asserts.isTrue(category == null, ContactError.CATEGORY_OF_QUN_EMPTY);
        qunVo.setCategoryName(category.getName());
        return qunVo;
    }


    public QunPlazaVO assembleQunPlaza(QunPlazaBO qunPlaza) {
        QunPlazaVO qunPlazaVO = new QunPlazaVO();
        List<QunBO> qunList = qunPlaza.getQunList();
        Map<Long, UserProfileDTO> userDicts = qunPlaza.getUserDicts();
        Map<Integer, Category> categories = qunPlaza.getCategoryDicts();
        Map<Integer, List<QunVO>> qunMap = new HashMap<>();
        for (QunBO qunBO : qunList) {
            QunVO qunVO;
            try {
                UserProfileDTO userProfile= userDicts.get(qunBO.getOwnerId());
                qunVO = this.assembleQun(qunBO,this.userAssembler.userDto2ContactVo(userProfile));
            } catch (BusinessException e) {
                logger.error("qun assemble error qunId:{},qunName:{}", qunBO.getId(), qunBO.getName(), e);
                continue;
            }
            if (!qunMap.containsKey(qunVO.getCategoryId())) {
                qunMap.put(qunVO.getCategoryId(), new ArrayList<>());
            }
            qunMap.get(qunVO.getCategoryId()).add(qunVO);
        }
        qunPlazaVO.setQunMap(qunMap);
        Map<Integer, CategoryVO> categoryVOMap = new HashMap<>();
        for (Integer categoryId : categories.keySet()) {
            categoryVOMap.put(categoryId, this.assembleCategory(categories.get(categoryId)));
        }
        qunPlazaVO.setCategoryDicts(categoryVOMap);
        return qunPlazaVO;
    }

    private CategoryVO assembleCategory(Category category) {
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setId(category.getId());
        categoryVO.setCategoryName(category.getName());
        categoryVO.setDescription(category.getDescription());
        return categoryVO;
    }

    public List<QunMemberVO> assembleQunMember(QunMemberWrapBO qunMemberWrap) {
        if (qunMemberWrap == null) {
            return null;
        }
        if (CollectionsUtility.isNullOrEmpty(qunMemberWrap.getQunMemberBOS())) {
            return null;
        }
        List<QunMemberVO> qunMembers = new ArrayList<>(qunMemberWrap.getQunMemberBOS().size());
        for (QunMemberBO qunMemberBO : qunMemberWrap.getQunMemberBOS()) {
            QunMemberVO qunMember = new QunMemberVO();
            qunMember.setUserId(qunMemberBO.getMemberId());
            qunMember.setNationality(Nationality.CHINA.getName());
            qunMember.setFlagUrl(Nationality.CHINA.getFlag());
            UserProfileDTO userProfile = qunMemberWrap.getUserProfileDTOMap().get(qunMember.getUserId());
            if (userProfile != null) {
                qunMember.setUserName(userProfile.getUserName());
                qunMember.setAvatar(userProfile.getAvatar());
            }
            qunMembers.add(qunMember);
        }
        return qunMembers;
    }
}
