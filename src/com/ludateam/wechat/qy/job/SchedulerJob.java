package com.ludateam.wechat.qy.job;
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
 * Created by Him on 2017/7/24.
 */


import com.jfinal.log.Log;
import com.jfinal.qyweixin.sdk.msg.send.Article;
import com.jfinal.qyweixin.sdk.msg.send.News;
import com.jfinal.qyweixin.sdk.msg.send.QiYeNewsMsg;
import com.ludateam.wechat.qy.utils.QyWeixinAPI;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SchedulerJob implements Job {
    private static final Log log = Log.getLog(SchedulerJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            // 调度任务参数
            if (log.isInfoEnabled()) log.info("待办任务定时任务开始");
            //scheduler();
            if (log.isInfoEnabled()) log.info("待办任务定时任务结束");
        } catch (Exception e) {
            if (log.isErrorEnabled()) log.error("待办任务定时任务异常", e);
        }
    }

    private void scheduler() {
        QiYeNewsMsg qiYeNewsMsg = new QiYeNewsMsg();
        qiYeNewsMsg.setTouser("Him|Jetzzzzz");

        qiYeNewsMsg.setAgentid("6");
        qiYeNewsMsg.setArticleCount(1);
        News qyNew = new News();

        Article article = new Article();

        article.setTitle("您有一个新的待办任务，请关注\r\n会议名称：月度办公会议\r\n开始时间：2017-07-24");
        article.setDescription("您有一个新的待办任务，请关注\n" +
                "会议名称:月度办公会议\n" +
                "开始时间:2017-07-24\n" +
                "结束时间:2017-07-24");
        article.setUrl("/wechat/myscheduler/add");
        List<Article> articles = new ArrayList<Article>();
        articles.add(article);

        qyNew.setArticles( articles);


        qiYeNewsMsg.setNews(qyNew);
        QyWeixinAPI.sendNewsMessage(qiYeNewsMsg);
    }
}
