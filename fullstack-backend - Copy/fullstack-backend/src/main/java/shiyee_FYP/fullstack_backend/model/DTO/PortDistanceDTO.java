package shiyee_FYP.fullstack_backend.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import shiyee_FYP.fullstack_backend.model.Port;

@Data
@AllArgsConstructor
public class PortDistanceDTO {
    private Port port;
    private Double distance;
    // 如果需要JSON序列化，添加无参构造器
    public PortDistanceDTO() {}
}