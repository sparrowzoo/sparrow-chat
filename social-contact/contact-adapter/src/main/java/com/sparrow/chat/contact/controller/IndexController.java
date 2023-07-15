package com.sparrow.chat.contact.controller;

import com.sparrow.chat.contact.assembler.QunAssembler;
import com.sparrow.chat.contact.bo.QunPlazaBO;
import com.sparrow.chat.contact.protocol.vo.QunPlazaVO;
import com.sparrow.chat.contact.service.QunService;
import com.sparrow.protocol.BusinessException;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;

@RestController
public class IndexController {

    @Inject
    private QunService qunService;

    @Inject
    private QunAssembler qunAssembler;

    @GetMapping("index")
    @ApiOperation("群广场")
    public ModelAndView qunPlaza() throws BusinessException {
        QunPlazaBO qunPlaza = this.qunService.qunPlaza();
        QunPlazaVO qunPlazaVo = this.qunAssembler.assembleQunPlaza(qunPlaza);
        ModelAndView mv = new ModelAndView("/index");
        mv.addObject(qunPlazaVo);
        return mv;
    }
}
