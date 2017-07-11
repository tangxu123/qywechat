package com.ludateam.wechat.qy.mvc.menu;

import com.jfinal.aop.Before;
import com.jfinal.log.Log;
import com.platform.annotation.Controller;
import com.platform.mvc.base.BaseController;
import com.platform.mvc.base.BaseModel;


/*
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
@Controller("/wechat/menu")
public class QyMenuTransController extends BaseController {
    private static final Log log = Log.getLog(QyMenuTransController.class);

    private CxMenuService menuService;
    /**
     * 列表
     */
    public void index() {
        paging(splitPage, BaseModel.sqlId_splitPageSelect, Menu.sqlId_splitPageFrom);
        render("/menu/list.html");
    }
    /**
     * 保存
     */
    @Before(MenuValidator.class)
    public void save() {
        Menu menu = getModel(Menu.class);

        menu.save();
        forwardAction("/wechat/menu/backOff");
    }

    /**
     * 查看
     */
    public void view() {
        Menu menu = Menu.dao.findById(getPara());
        setAttr("menu", menu);
        render("/menu/view.html");
    }
    /**
     * 准备更新
     */
    public void edit() {
        Menu menu = Menu.dao.findById(getPara());
        setAttr("menu", menu);
        render("/menu/update.html");
    }

    /**
     * 更新
     */
    @Before(MenuValidator.class)
    public void update() {
        getModel(Menu.class).update();
        forwardAction("/wechat/menu/backOff");
    }

    /**
     * 删除
     */
    public void delete() {
        menuService.baseDelete("wechat_menus", getPara() == null ? ids : getPara());
        forwardAction("/wechat/menu/backOff");
    }
}
