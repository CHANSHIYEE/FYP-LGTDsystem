package shiyee_FYP.fullstack_backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "location_relations") // 必须与实际表名一致
public class LocRelation {


    public enum RelationType {
//        SUPPLY, RESEARCH, TRANSPORT // 注意枚举值必须与数据库值完全匹配
        RESEARCH,WAREHOUSE,FACTORY,SUPPLIER,PROTOTYPE,DISTRIBUTION,MANUFACTURER,TESTING,
SILICON_WAFER,SEMI_MATERIAL,TARGET_MATERIAL,PROTOTYPE_FLOW,PHOTONICS_FLOW,STORAGE_FLOW,PACKAGE_TEST,FINISHED_GOODS,JUST_IN_TIME,BACKUP_DELIVERY,CHIP_FAB_MAIN,CHIP_FAB_BACKUP,TARGET_MATERIAL_BACKUP,SILICON_WAFER_BACKUP
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "source_id", nullable = false)
    private Location source;

    @ManyToOne
    @JoinColumn(name = "target_id", nullable = false)
    private Location target;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation_type", nullable = false)
    private RelationType relationType;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Location getSource() {
        return source;
    }

    public void setSource(Location source) {
        this.source = source;
    }

    public Location getTarget() {
        return target;
    }

    public void setTarget(Location target) {
        this.target = target;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}