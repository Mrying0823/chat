package org.mrying.chat.view;

import org.mrying.chat.enums.RespCode;

import java.util.List;
import java.util.Map;

/**
 * @author 邓和颖
 * @since 2023/9/24 1:13
 */

// 统一应答结果类，controller 返回值
public class RespResult {

    // 应答码
    private int code;

    // code 的文字说明，用于提示
    private String msg;

    // 单个数据
    private Object retData;

    // 集合数据
    private List list;

    // 使用映射（Map）返回数据
    private Map<String,Object> map;

    private String accessToken;

    private String conversationId;

    // 笔记专题 Id
    private String noteCategoryId;

    // 笔记 Id
    private String noteId;

    // 表示成功的 RespResult 对象
    public static RespResult ok() {
        RespResult respResult = new RespResult();
        respResult.setCode(RespCode.SUCCESS.getCode());
        respResult.setMsg(RespCode.SUCCESS.getMsg());
        return respResult;
    }

    // 表示失败的 RespResult 对象
    public static RespResult fail() {
        RespResult respResult = new RespResult();
        respResult.setCode(RespCode.BAD_REQUEST.getCode());
        respResult.setMsg(RespCode.BAD_REQUEST.getMsg());
        return respResult;
    }

    public static RespResult notFound() {
        RespResult respResult = new RespResult();
        respResult.setCode(RespCode.NOT_FOUND.getCode());
        respResult.setMsg(RespCode.NOT_FOUND.getMsg());
        return respResult;
    }

    public void setRespCode(RespCode respCode) {
        this.code = respCode.getCode();
        this.msg = respCode.getMsg();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getNoteCategoryId() {
        return noteCategoryId;
    }

    public void setNoteCategoryId(String noteCategoryId) {
        this.noteCategoryId = noteCategoryId;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
