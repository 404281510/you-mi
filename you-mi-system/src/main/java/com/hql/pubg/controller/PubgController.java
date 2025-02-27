package com.hql.pubg.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.gplnature.pubgapi.api.PubgClient;
import com.github.gplnature.pubgapi.exception.PubgClientException;
import com.github.gplnature.pubgapi.model.ExtendedPlatform;
import com.github.gplnature.pubgapi.model.MiddlePlatform;
import com.github.gplnature.pubgapi.model.match.Match;
import com.github.gplnature.pubgapi.model.match.MatchResponse;
import com.github.gplnature.pubgapi.model.participant.Participant;
import com.github.gplnature.pubgapi.model.participant.ParticipantStats;
import com.github.gplnature.pubgapi.model.player.Player;
import com.hql.db.aop.anno.DataSourceType;
import com.hql.pubg.dao.SystemTenantMapper;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hql
 * @className FirstController
 * @description TODO
 * @date 2023/9/4 14:07
 */
@Controller
@Configuration
public class PubgController {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private SystemTenantMapper systemTenantMapper;

    @RequestMapping(value = "testPUBG" ,method = RequestMethod.GET)
    @ResponseBody
    public JSONObject testPUBG(String  pubgName) throws PubgClientException {
        // Create a pubg client to make requests to the API
        PubgClient pubgClient = new PubgClient("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJmOGVmMTM5MC01ZGRmLTAxM2QtYmYxMS0xNjVmNmJlZjNhYmMiLCJpc3MiOiJnYW1lbG9ja2VyIiwiaWF0IjoxNzI3MzE5MDExLCJwdWIiOiJibHVlaG9sZSIsInRpdGxlIjoicHViZyIsImFwcCI6Ii1jOGNkOGY3Ni05ZjU4LTQ1MTItODJjNy02Y2U0ZDYxNjkxMzEifQ.fIRTxxbXZyVZqAPJ0SEBa01RTbkcm1FpnBLWYwhl4M8");

        // Get a list of players using their names in-game
        List<Player> playerList = pubgClient.getPlayersByNames(MiddlePlatform.STEAM, pubgName);
//        List<Player> playerList = pubgClient.getPlayersByNames(MiddlePlatform.STEAM, "Maqianlizzz");
//        List<Player> playerList = pubgClient.getPlayersByNames(MiddlePlatform.STEAM, "dakonglongaxu");

        Player player = playerList.get(0);

        System.out.println(player.getAttributes().getName());
        String matchId = player.getRelationships().getMatches().get(0).getId();
        System.out.println(matchId);
        List<Match> matches = player.getRelationships().getMatches().stream().
                filter(o -> o instanceof Match).map(o -> (Match)o).
                collect(Collectors.toList());
        JSONObject jsonObject = new JSONObject();
        double d = 0;
        int num = 0;
        for (Match match: matches) {
            MatchResponse match1 = pubgClient.getMatch(ExtendedPlatform.STEAM, match.getId());
            LocalDateTime localDateTime = match1.getData().getAttributes().getCreatedAt().withZoneSameInstant(ZoneId.of("Asia/Shanghai")).toLocalDateTime();
            if (LocalDateTime.now().minusDays(1).isBefore(localDateTime)) {
                List<Participant> participantListx = match1.getIncluded().stream().
                        filter(o -> o instanceof Participant).
                        map(o -> (Participant)o).collect(Collectors.toList());
                for (Participant participant : participantListx) {
                    ParticipantStats participantStats = participant.getAttributes().getStats();
                    if (participantStats.getPlayerId().equals(player.getId())) {
                        d = d +participantStats.getDamageDealt();
                        num++;
                        System.out.println(localDateTime + "---"+ JSON.toJSONString(participant.getAttributes().getStats()));
                    }
                }
            } else {
                break;
            }
        }
        jsonObject.put("num",num);
        jsonObject.put("damageDealt",String.format("%.2f",d));
        return jsonObject;
    }

    @RequestMapping(value = "testDB" ,method = RequestMethod.GET)
    @ResponseBody
    @DataSourceType("second")
    public void testDB(String  pubgName)  {
//        sqlSessionTemplate.getSqlSessionFactory().openSession().selectList("select * from system_tenant");
//        List<Map<String,Object>> mapList = jdbcTemplate.queryForList("select * from system_tenant");
        List<Map<String,Object>> mapList1 = systemTenantMapper.selectTenant();
        mapList1.forEach(map -> {
            System.out.println(map.entrySet());
        });

    }
}
