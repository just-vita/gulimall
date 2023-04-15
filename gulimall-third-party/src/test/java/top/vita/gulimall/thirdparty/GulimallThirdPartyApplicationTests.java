package top.vita.gulimall.thirdparty;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.vita.gulimall.thirdparty.utils.ConnectTencentCloud;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallThirdPartyApplicationTests {

//    @Autowired
//    private CosProperties cosProperties;
    @Autowired
    private ConnectTencentCloud tencentCloud;

    @Test
    public void contextLoads() {
        System.out.println(tencentCloud);
    }

}
