package shiyee_FYP.fullstack_backend.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shiyee_FYP.fullstack_backend.model.ShippingLane;
import shiyee_FYP.fullstack_backend.repository.PortRepository;
import shiyee_FYP.fullstack_backend.repository.ShippingLaneRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShippingLaneService {
    private final ShippingLaneRepository shippingLaneRepository;
    private final PortRepository portRepository;

    /**
     * 查找最佳海运航线（支持中转）
     */
    public Optional<ShippingLane> findBestShippingLane(Long sourcePortId, Long targetPortId) {
        // 1. 先尝试查找直达航线
        Optional<ShippingLane> directLane = shippingLaneRepository.findDirectLane(sourcePortId, targetPortId);
        if (directLane.isPresent()) {
            return directLane;
        }

        // 2. 查找中转航线（最多一次中转）
        return findLaneWithOneTransfer(sourcePortId, targetPortId);
    }

    private Optional<ShippingLane> findLaneWithOneTransfer(Long sourcePortId, Long targetPortId) {
        // 查找从起点港口出发的所有航线
        List<ShippingLane> fromSource = shippingLaneRepository.findByStartPortId(sourcePortId);

        // 查找到达目标港口的所有航线
        List<ShippingLane> toTarget = shippingLaneRepository.findByEndPortId(targetPortId);

        // 寻找共同的中转港口
        for (ShippingLane lane1 : fromSource) {
            Long intermediatePortId = lane1.getEndPort().getId();

            for (ShippingLane lane2 : toTarget) {
                if (lane2.getStartPort().getId().equals(intermediatePortId)) {
                    // 创建虚拟的"中转航线"
                    ShippingLane combinedLane = new ShippingLane();
                    combinedLane.setStartPort(lane1.getStartPort());
                    combinedLane.setEndPort(lane2.getEndPort());
                    combinedLane.setDistance(lane1.getDistance() + lane2.getDistance());
                    combinedLane.setEstimatedTime(lane1.getEstimatedTime() + lane2.getEstimatedTime());
                    combinedLane.setViaPorts(intermediatePortId.toString());
                    return Optional.of(combinedLane);
                }
            }
        }

        return Optional.empty();
    }
}