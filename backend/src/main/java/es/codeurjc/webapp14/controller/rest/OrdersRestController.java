package es.codeurjc.webapp14.controller.rest;

import es.codeurjc.webapp14.dto.OrderDTO;
import es.codeurjc.webapp14.model.Order;
import es.codeurjc.webapp14.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Admin_Orders", description = "Endpoints for managing Orders as an admin")
public class OrdersRestController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "Get Orders", description = "Return all the Orders created")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAdminOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> data = new HashMap<>();

        // Get total orders
        data.put("totalOrders", orderService.getTotalOrders());

        // Get paginated orders
        // Refactor? products paginated has hasMore and nextPage attributes?
        data.put("orders", orderService.getOrdersPaginated(page, size));

        return ResponseEntity.ok(data);
    }

    @Operation(summary = "Edit Order", description = "Edit a created Order")
    @PatchMapping("/{orderId}")
    public OrderDTO editOrder(
        @PathVariable Long orderId, @RequestParam(value = "newState", required = false, defaultValue = "Pagado") String newState) {

        if(!newState.equals(Order.State.Enviado.name()) && !newState.equals(Order.State.Pagado.name()) && !newState.equals(Order.State.Procesado.name())){
            throw new AccessDeniedException("You do not have permission to edit this order to " + newState + " state");

        }

        Order.State orderState;

        if(newState.equals(Order.State.Enviado.name())){
            orderState = Order.State.Enviado;
        }
        else if(newState.equals(Order.State.Pagado.name())){
            orderState = Order.State.Pagado;
        }
        else{
            orderState = Order.State.Procesado;
        }

        OrderDTO order = orderService.editOrder(orderId,orderState);
    
        return order;
    }

    @Operation(summary = "Delete Order", description = "Delete a created Order")
    @DeleteMapping("/{orderId}")
    public OrderDTO deleteOrder(
        @PathVariable Long orderId) {

        OrderDTO order = orderService.deleteOrder(orderId);    

        return order;
    }


}
