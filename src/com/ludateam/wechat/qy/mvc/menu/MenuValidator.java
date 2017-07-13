package com.ludateam.wechat.qy.mvc.menu;/*
 * Copyright 2017 Luda Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Created by Him on 2017/7/11.
 */

import com.jfinal.core.Controller;
import com.platform.mvc.base.BaseValidator;
import com.test.mvc.blog.Blog;

public class MenuValidator extends BaseValidator {

    /**
     * Use validateXxx method to validate the parameters of this action.
     *
     * @param c
     */
    @Override
    protected void validate(Controller c) {
        String actionKey = getActionKey();
        if (actionKey.equals("/wechat/menu/save")){
            // validateString("username", 6, 30, "usernameMsg", "请输入登录账号!");

        } else if (actionKey.equals("/wechat/menu/update")){

        }
    }

    /**
     * Handle the validate error.
     * Example:<br>
     * controller.keepPara();<br>
     * controller.render("register.html");
     *
     * @param c
     */
    @Override
    protected void handleError(Controller c) {
        controller.keepModel(Menu.class);

        String actionKey = getActionKey();
        if (actionKey.equals("/wechat/menu/save")){
            controller.render("/test/xxx.html");

        } else if (actionKey.equals("/wechat/menu/update")){
            controller.render("/test/xxx.html");

        }
    }
}
