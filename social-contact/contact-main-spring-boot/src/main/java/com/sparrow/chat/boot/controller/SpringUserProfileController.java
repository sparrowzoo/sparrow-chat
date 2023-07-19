package com.sparrow.chat.boot.controller;

import com.sparrow.passport.controller.UserProfileController;
import com.sparrow.passport.controller.protocol.vo.BasicUserVO;
import com.sparrow.passport.protocol.param.AvatarModifyParam;
import com.sparrow.protocol.BusinessException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.io.IOException;

@RestController
public class SpringUserProfileController {
    @Inject
    private UserProfileController userProfileController;

    @RequestMapping("my")
    public ModelAndView loadUserBasic() throws BusinessException {
        BasicUserVO basicUserVO = this.userProfileController.loadUserBasic();
        return new ModelAndView("/my", "profile", basicUserVO);
    }

    @RequestMapping("profile")
    public ModelAndView profile() throws BusinessException {
        BasicUserVO basicUserVO = this.userProfileController.loadUserBasic();
        return new ModelAndView("/profile", "profile", basicUserVO);
    }

    @RequestMapping("/modify-avatar")
    public ModelAndView loadUser() throws BusinessException {
        BasicUserVO basicUserVO = this.userProfileController.loadUserBasic();
        return new ModelAndView("/modify-avatar", "profile", basicUserVO);
    }

    @RequestMapping("modify-user-avatar")
    public String modifyAvatar(@RequestBody AvatarModifyParam avatarModifyParam) throws BusinessException, IOException {
        return this.userProfileController.modifyAvatar(avatarModifyParam);
    }
}
