package com.example.backendmainserver.websocket.application;

import com.example.backendmainserver.PowerData.domain.PowerData;
import com.example.backendmainserver.PowerData.domain.PowerDataList;
import com.example.backendmainserver.domain.user.application.UserService;
import com.example.backendmainserver.domain.user.domain.User;
import com.example.backendmainserver.domain.user.domain.UserVO;
import com.example.backendmainserver.websocket.domain.InMemoryWebSocketSessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebSocketSessionService {
    private final InMemoryWebSocketSessionRepository webSocketSessionRepository;
    private final UserService userService;

    public void save(WebSocketSession session, Long userId){
        User user = userService.getUser(userId);

        UserVO userVO = UserVO.buildUserVO(user);
        webSocketSessionRepository.save(session, userVO);
    }

    public void delete(String sessionId){
        webSocketSessionRepository.delete(sessionId);
    }

    public void sendPowerData(PowerDataList data) throws IOException {
        Set<Map.Entry<WebSocketSession, UserVO>> entrySet = webSocketSessionRepository.getEntrySet();
        List<PowerData> powerDataList = data.getPowerDataList();
        ObjectMapper objectMapper = new ObjectMapper();

        for (Map.Entry<WebSocketSession, UserVO> webSocketSessionUserVOEntry : entrySet) {
            WebSocketSession webSocketSession = webSocketSessionUserVOEntry.getKey();
            UserVO userVO = webSocketSessionUserVOEntry.getValue();
            List<PowerData> sendPowerDataList = new ArrayList<>();

            List<Long> userPortsId = userService.getPortsId(userVO.getId());
            for (PowerData powerData : powerDataList) {
                Long portId = powerData.getPortId();

                if (userPortsId.contains(portId)) {
                    sendPowerDataList.add(powerData);
                }
            }

            if(!sendPowerDataList.isEmpty()) {
                String jsonMessage = objectMapper.writeValueAsString(sendPowerDataList); // 리스트를 JSON 문자열로 변환
                webSocketSession.sendMessage(new TextMessage(jsonMessage));

                log.info("Main Server Send Data To User #{} ", userVO.getId());
                log.info("Data = {}", sendPowerDataList);
            }
        }
    }
}
