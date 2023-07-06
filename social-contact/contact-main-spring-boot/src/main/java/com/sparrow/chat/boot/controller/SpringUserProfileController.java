package com.sparrow.chat.boot.controller;

import com.sparrow.passport.controller.UserProfileController;
import com.sparrow.passport.controller.protocol.vo.BasicUserVO;
import com.sparrow.passport.protocol.param.AvatarModifyParam;
import com.sparrow.protocol.BusinessException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.IOException;

@RestController
@RequestMapping("/profile")
public class SpringUserProfileController {
    @Inject
    private UserProfileController userProfileController;

    @RequestMapping("load-user-profile")
    BasicUserVO loadUserBasic() throws BusinessException {
        return this.userProfileController.loadUserBasic();
    }

    @RequestMapping("modify-user-avatar")
    public String modifyAvatar(@RequestBody AvatarModifyParam avatarModifyParam) throws BusinessException, IOException {
      return   this.userProfileController.modifyAvatar(avatarModifyParam);
    }
}
