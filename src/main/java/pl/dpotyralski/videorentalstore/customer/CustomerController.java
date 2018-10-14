package pl.dpotyralski.videorentalstore.customer;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dpotyralski.videorentalstore.customer.protocol.BonusPointsResponse;

@RestController
@RequestMapping("/customers")
@AllArgsConstructor
class CustomerController {

    private CustomerFacade customerFacade;

    @GetMapping("/{id}/bonus-points")
    @ApiOperation(value = "Customer bonus points")
    public BonusPointsResponse getCustomerBonusPoints(@PathVariable Long id) {
        return new BonusPointsResponse(customerFacade.getCustomerBonusPoints(id));
    }
}
