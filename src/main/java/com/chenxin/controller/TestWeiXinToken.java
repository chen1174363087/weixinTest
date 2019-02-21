package com.chenxin.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;

@RestController
public class TestWeiXinToken {

    Logger log = Logger.getLogger(TestWeiXinToken.class);

    @RequestMapping("/test")
    public String confirm() {
        return "signature";
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public String confirm(@RequestParam(value = "signature", required = true) String signature,
                          @RequestParam(value = "timestamp", required = true) String timestamp, @RequestParam(value = "nonce", required = true) String nonce) {
        System.out.println("hello");
        return signature;
    }

    /**
     * 接通微信服务器
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/confirm1", method = RequestMethod.GET)
    public void confirm(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("sdsdf");
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            if (checkSignature1(signature, timestamp, nonce)) {
                //如果校验成功，将得到的随机字符串原路返回
                out.print(echostr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    /**
     * 被动回复消息
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/confirm1", method = RequestMethod.POST)
    public String replyMessage(HttpServletRequest request, HttpServletResponse response) {

        String requestMessage = JSONObject.toJSONString(request.getParameterMap());

        return  requestMessage;
    }

    @RequestMapping(value = "toMsg",method = RequestMethod.POST,produces = {"application/xml; charset=UTF-8"})
    public void toMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("微信返回了--------Weichart_Return");
        String resXml="";
        InputStream inputStream ;
        StringBuffer sb = new StringBuffer();
        inputStream = request.getInputStream();
        String reqXmlStr;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((reqXmlStr = in.readLine()) != null){
            sb.append(reqXmlStr);
        }
        in.close();
        inputStream.close();
        log.info("微信返回了的数据--------");
        try {
            Map<String, Object> map = JSONObject.parseObject(sb.toString());
            String toUserName = map.get("ToUserName").toString();//开发者微信号
            String fromUserName = map.get("FromUserName").toString();//发送方帐号（一个OpenID）
            String createTime = map.get("CreateTime").toString();//消息创建时间 （整型）
            String msgType = map.get("MsgType").toString();//消息类型
            String content = map.get("Content").toString();//消息内容
            String msgId = map.get("MsgId").toString();//消息id，64位整型
            log.info("接收到的消息：\r\n"+"ToUserName="+toUserName+"\r\nFromUserName="+fromUserName+"\r\nCreateTime="+
                    createTime+"\r\nMsgType="+msgType+"\r\nContent="+content+"\r\nMsgId="+msgId);
            String resXmlStr="<xml><ToUserName><![CDATA["+fromUserName+"]]></ToUserName>" +//此处要填写 发送方帐号（一个OpenID）
                    "<FromUserName><![CDATA["+toUserName+"]]></FromUserName>" +//此处填写开发者微信号
                    "<CreateTime>"+createTime+"</CreateTime>" +
                    "<MsgType><![CDATA["+msgType+"]]></MsgType>"+
                    "<Content><![CDATA["+content+"]]></Content></xml>";
            log.info(resXmlStr);
            response.getWriter().print(resXmlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final String tooken = "chenxin520"; //开发者自行定义Tooken

    public boolean checkSignature1(String signature, String timestamp, String nonce) {
//1.定义数组存放tooken，timestamp,nonce
        String[] arr = {tooken, timestamp, nonce};
//2.对数组进行排序
        Arrays.sort(arr);
//3.生成字符串
        StringBuffer sb = new StringBuffer();
        for (String s : arr) {
            sb.append(s);
        }
//4.sha1加密,网上均有现成代码
        String temp = getSha1(sb.toString());
//5.将加密后的字符串，与微信传来的加密签名比较，返回结果
        return temp.equals(signature);
    }

    public String getSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

}
