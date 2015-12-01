package com.shuangge.english.support.notice;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

import com.shuangge.english.GlobalApp;
import com.shuangge.english.support.debug.DebugPrinter;

/**
 * 可以做消息通知
 * @author Jeffrey
 *
 */
public class FetchNewMsgService extends IntentService {


    public static Intent newIntentFromAlarmManager() {
        Intent intent = new Intent(GlobalApp.getInstance(), FetchNewMsgService.class);
        intent.setAction(ACTION_ALARM_MANAGER);
        return intent;
    }

    public static Intent newIntentFromOpenApp() {
        Intent intent = new Intent(GlobalApp.getInstance(), FetchNewMsgService.class);
        intent.setAction(ACTION_OPEN_APP);
        return intent;

    }

    private static final String ACTION_ALARM_MANAGER = "org.qii.weiciyuan:alarmmanager";
    private static final String ACTION_OPEN_APP = "org.qii.weiciyuan:openapp";

    //close service between 1 clock and 8 clock
    private static final int NIGHT_START_TIME_HOUR = 1;

    private static final int NIGHT_END_TIME_HOUR = 7;


    public FetchNewMsgService() {
        super("FetchNewMsgService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    	/*
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        if (ACTION_ALARM_MANAGER.equals(action)) {
            DebugPrinter.i("FetchNewMsgService is started by " + ACTION_ALARM_MANAGER);
            if (SettingUtility.disableFetchAtNight() && isNowNight()) {
                DebugPrinter.i("FetchNewMsgService is disabled at night, so give up");
                return;
            }
        } 
        else if (ACTION_OPEN_APP.equals(action)) {
            //empty
            DebugPrinter.i("FetchNewMsgService is started by " + ACTION_OPEN_APP);
        }
        else {
            throw new IllegalArgumentException("Intent action is empty");
        }

        List<AccountBean> accountBeanList = AccountDBTask.getAccountList();
        if (accountBeanList.size() == 0) {
            return;
        }
        for (AccountBean account : accountBeanList) {
            try {
                DebugPrinter.i("FetchNewMsgService start fetch " + account.getUsernick() + "'s unread data");
                fetchMsg(account);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    	 */
        DebugPrinter.i("FetchNewMsgService finished");
    }

/*
    private boolean isNowNight() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        return hour >= NIGHT_START_TIME_HOUR && hour <= NIGHT_END_TIME_HOUR;
    }


    private void fetchMsg(AccountBean accountBean) throws WeiboException {
        CommentListBean commentResult = null;
        MessageListBean mentionStatusesResult = null;
        CommentListBean mentionCommentsResult = null;
        UnreadBean unreadBean = null;

        String token = accountBean.getAccess_token();

        UnreadDao unreadDao = new UnreadDao(token, accountBean.getUid());
        unreadBean = unreadDao.getCount();
        if (unreadBean == null) {
            return;
        }
        int unreadCommentCount = unreadBean.getCmt();
        int unreadMentionStatusCount = unreadBean.getMention_status();
        int unreadMentionCommentCount = unreadBean.getMention_cmt();

        if (unreadCommentCount > 0 && SettingUtility.allowCommentToMe()) {
            MainCommentsTimeLineDao dao = new MainCommentsTimeLineDao(token);
            CommentListBean oldData = null;
            CommentTimeLineData commentTimeLineData = CommentToMeTimeLineDBTask
                    .getCommentLineMsgList(accountBean.getUid());
            if (commentTimeLineData != null) {
                oldData = commentTimeLineData.cmtList;
            }
            if (oldData != null && oldData.getSize() > 0) {
                dao.setSince_id(oldData.getItem(0).getId());
            }

            commentResult = dao.getGSONMsgListWithoutClearUnread();


        }

        if (unreadMentionStatusCount > 0 && SettingUtility.allowMentionToMe()) {
            MentionsWeiboTimeLineDao dao = new MentionsWeiboTimeLineDao(token);
            MessageListBean oldData = null;
            MentionTimeLineData mentionStatusTimeLineData = MentionWeiboTimeLineDBTask
                    .getRepostLineMsgList(accountBean.getUid());
            if (mentionStatusTimeLineData != null) {
                oldData = mentionStatusTimeLineData.msgList;
            }
            if (oldData != null && oldData.getSize() > 0) {
                dao.setSince_id(oldData.getItem(0).getId());
            }
            mentionStatusesResult = dao.getGSONMsgListWithoutClearUnread();
        }

        if (unreadMentionCommentCount > 0 && SettingUtility.allowMentionCommentToMe()) {
            MainCommentsTimeLineDao dao = new MentionsCommentTimeLineDao(token);
            CommentListBean oldData = null;
            CommentTimeLineData commentTimeLineData = MentionCommentsTimeLineDBTask
                    .getCommentLineMsgList(accountBean.getUid());
            if (commentTimeLineData != null) {
                oldData = commentTimeLineData.cmtList;
            }
            if (oldData != null && oldData.getSize() > 0) {
                dao.setSince_id(oldData.getItem(0).getId());
            }
            mentionCommentsResult = dao.getGSONMsgListWithoutClearUnread();
        }

        clearDatabaseUnreadInfo(accountBean.getUid(), unreadBean.getMention_status(),
                unreadBean.getMention_cmt(), unreadBean.getCmt());

        boolean mentionsWeibo = (mentionStatusesResult != null
                && mentionStatusesResult.getSize() > 0);
        boolean mentionsComment = (mentionCommentsResult != null
                && mentionCommentsResult.getSize() > 0);
        boolean commentsToMe = (commentResult != null && commentResult.getSize() > 0);

        if (mentionsWeibo || mentionsComment || commentsToMe) {
            sendTwoKindsOfBroadcast(accountBean, commentResult, mentionStatusesResult,
                    mentionCommentsResult, unreadBean);
        } else {
//            NotificationManager notificationManager = (NotificationManager) getApplicationContext()
//                    .getSystemService(NOTIFICATION_SERVICE);
//            notificationManager.cancel(
//                    NotificationServiceHelper.getMentionsWeiboNotificationId(accountBean));
        }
    }

    private void clearDatabaseUnreadInfo(String accountId, int mentionsWeibo, int mentionsComment,
            int cmt) {
        if (mentionsWeibo == 0) {
            NotificationDBTask
                    .asyncCleanUnread(accountId, NotificationDBTask.UnreadDBType.mentionsWeibo);
        }
        if (mentionsComment == 0) {
            NotificationDBTask
                    .asyncCleanUnread(accountId, NotificationDBTask.UnreadDBType.mentionsComment);
        }
        if (cmt == 0) {
            NotificationDBTask
                    .asyncCleanUnread(accountId, NotificationDBTask.UnreadDBType.commentsToMe);
        }
    }

    private void sendTwoKindsOfBroadcast(AccountBean accountBean,
            CommentListBean commentResult,
            MessageListBean mentionStatusesResult,
            CommentListBean mentionCommentsResult,
            UnreadBean unreadBean) {

        DebugPrinter.i("Send unread data to ");

        if (unreadBean != null) {
            AppNotificationCenter.getInstance().addUnreadBean(accountBean, unreadBean);
        }
        if (mentionStatusesResult != null) {
            AppNotificationCenter.getInstance().addUnreadMentions(accountBean, mentionStatusesResult);
        }
        if (mentionCommentsResult != null) {
            AppNotificationCenter.getInstance().addUnreadMentionsComment(accountBean, mentionCommentsResult);
        }
        if (commentResult != null) {
            AppNotificationCenter.getInstance().addUnreadComments(accountBean, commentResult);
        }
        AppNotificationCenter.getInstance().refreshToUI(accountBean);
        AppNotificationCenter.getInstance().showAndroidNotification(accountBean);
    }
    */
}
