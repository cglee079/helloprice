package com.podo.helloprice.crawl.agent.job.config;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class CustomMapJobRepositoryFactoryBean extends MapJobRepositoryFactoryBean {

    @Override
    public JobExecutionDao createJobExecutionDao () throws Exception {
        final JobExecutionDao jobExecutionDao = super.createJobExecutionDao();
        return new JobExecutionDao() {

            @Override
            public void saveJobExecution(JobExecution jobExecution) {
                jobExecutionDao.saveJobExecution(jobExecution);
            }

            @Override
            public List<JobExecution> findJobExecutions(JobInstance jobInstance) {
                return jobExecutionDao.findJobExecutions(jobInstance);
            }

            @Override
            public JobExecution getLastJobExecution(JobInstance jobInstance) {
                return jobExecutionDao.getLastJobExecution(jobInstance);
            }

            @Override
            public Set<JobExecution> findRunningJobExecutions(String jobName) {
                return jobExecutionDao.findRunningJobExecutions(jobName);
            }

            @Override
            public JobExecution getJobExecution(Long executionId) {
                return jobExecutionDao.getJobExecution(executionId);
            }

            @Override
            public void synchronizeStatus(JobExecution jobExecution) {
            }

            @Override
            public void updateJobExecution(JobExecution jobExecution) {
                //Job 실행 후 가장 마지막에 호출되는 메소드
                //모든 InMemory Job Log를 삭제
                //좋지 않은 코딩. 임시로 사용.
                CustomMapJobRepositoryFactoryBean.this.clear();
            }
        };
    }
}
