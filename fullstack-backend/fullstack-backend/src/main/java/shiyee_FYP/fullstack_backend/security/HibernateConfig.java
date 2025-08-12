package shiyee_FYP.fullstack_backend.security;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.spatial.integration.SpatialService;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig implements TypeContributor {
    @Override
    public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        // 对于 Hibernate 6.x，不需要手动注册 SpatialService
        // 只需确保正确的方言配置即可
    }
}