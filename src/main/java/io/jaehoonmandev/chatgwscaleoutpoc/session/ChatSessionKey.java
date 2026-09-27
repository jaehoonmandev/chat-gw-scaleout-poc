package io.jaehoonmandev.chatgwscaleoutpoc.session;

public record ChatSessionKey(String senderKey, String userKey) {
    // ":" 구분자가 없으면 senderKey="ab"+userKey="c"와 senderKey="a"+userKey="bc"가 같은 키가 된다.
    public String asChatSessionKey() {return senderKey + ":" + userKey;}
}
