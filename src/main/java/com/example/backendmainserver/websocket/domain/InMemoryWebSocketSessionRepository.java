package com.example.backendmainserver.websocket.domain;

import com.example.backendmainserver.user.domain.UserVO;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryWebSocketSessionRepository {
    private Map<WebSocketSession, UserVO> store = new ConcurrentHashMap<>();

    public void save(WebSocketSession session, UserVO userVO){
        store.put(session, userVO);
    }

    public void delete(String sessionId){
        WebSocketSession deleteObj = null;

        for (WebSocketSession webSocketSession : store.keySet()) {
            if (webSocketSession.getId() == sessionId){
                deleteObj = webSocketSession;
                break;
            }
        }

        if (deleteObj != null)
            store.remove(deleteObj);
    }

    public Set<Map.Entry<WebSocketSession, UserVO>> getEntrySet(){
        return store.entrySet();
    }
}
