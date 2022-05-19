package com.ljf.ploughthewaves.interfaces;

import com.ljf.ploughthewaves.domain.admin.repository.IUserRepository;
import com.ljf.ploughthewaves.domain.poista.service.util.SolvedNumbersApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private SolvedNumbersApi solvedNumbersApi;

    @Resource
    private IUserRepository userRepository;

    /**
     * 匿名用户获取刷题数
     * @param ojName
     * @param handle
     * @return
     */
    @PostMapping("/getSolvedNumber")
    public int getSolvedNumber(String ojName, String handle) throws IOException {
        log.info("匿名用户查询:{} - {}",ojName,handle);
        return solvedNumbersApi.getSolvedNumbers(ojName, handle);
    }

    /**
     * 更新统计信息
     * @param openid
     * @return
     */
    @PostMapping("/updateStatisticsInfo")
    public String update(@RequestParam String openid) {
        userRepository.updateStatisticsInfo(openid);
        return null;
    }
}
