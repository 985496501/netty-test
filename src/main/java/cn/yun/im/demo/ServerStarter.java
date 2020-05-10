package cn.yun.im.demo;

import cn.yun.im.demo.discard.DiscardServer;

/**
 * @author: Liu Jinyun
 * @date: 2020/5/10/23:36
 */
public class ServerStarter {
    public static void main(String[] args) {
        try {
            new DiscardServer(8080).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
