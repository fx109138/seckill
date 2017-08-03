package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.SecKill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by fx on 2017/2/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<SecKill> list=seckillService.getSeckillList();
        logger.info("list={}",list);
    }

    @Test
    public void getById() throws Exception {
        long id=1001L;
        SecKill secKill=seckillService.getById(id);
        logger.info("seckill={}",secKill);
    }

    @Test
    public void testSeckillLogic() throws Exception {
        long id=1001L;
        Exposer exposer=seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()){
            logger.info("exposer={}",exposer);
            long phone=12345638365L;
            String md5=exposer.getMd5();
            try {
                SeckillExecution seckillExecution=seckillService.executeSeckill(id,phone,md5);
                logger.info("result={}",seckillExecution);
            } catch (RepeatKillException e) {
                logger.error(e.getMessage(),e);
            } catch (SeckillCloseException e){
                logger.error(e.getMessage(),e);
            }
        }else {
            logger.warn("expposer={}",exposer);
        }
    }

    @Test
    public void executeSeckillProcedure(){
        long seckillId=1003;
        long phone=12309875678L;
        Exposer exposer=seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()){
            String md5=exposer.getMd5();
            SeckillExecution execution=seckillService.executeSeckillProcedure(seckillId,phone,md5);
            logger.info(execution.getStateInfo());
        }
    }

}