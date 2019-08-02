package life.hk1221.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("life.hk1221.community.mapper")
public class CommnuityApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommnuityApplication.class, args);
    }

}
