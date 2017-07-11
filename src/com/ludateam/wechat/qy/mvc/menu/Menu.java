package com.ludateam.wechat.qy.mvc.menu;

import com.jfinal.log.Log;
import com.platform.annotation.Table;
import com.platform.mvc.base.BaseModel;
import com.test.mvc.blog.Blog;

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
@SuppressWarnings("unused")
@Table(tableName = "wechat_menus",pkName = "id")
public class Menu extends BaseModel<Menu> {
    private static final Log log = Log.getLog(Menu.class);

    public static final Menu dao = new Menu().dao();

    /**
     * 字段描述：主键
     * 字段类型：character varying  长度：32
     */

    public static final String column_ids = "id";

    /**
     * 字段描述：标题
     * 字段类型：character varying  长度：200
     */
    public static final String column_name = "name";

    /**
     * 字段描述：内容
     * 字段类型：text  长度：null
     */
    public static final String column_link = "link";

    public static final String sqlId_splitPageFrom = "wechat.menu.splitPageFrom";

    private String ids;
    private String name;
    private String link;

    public void setIds(String ids) {
        set(column_ids, ids);
    }

    public String getIds() {
        return get(column_ids);
    }

    public void setTitle(String title) {
        set(column_name, title);
    }

    public String getTitle() {
        return get(column_name);
    }

    public void setContent(String content) {
        set(column_link, content);
    }

    public String getContent() {
        return get(column_link);
    }

}
