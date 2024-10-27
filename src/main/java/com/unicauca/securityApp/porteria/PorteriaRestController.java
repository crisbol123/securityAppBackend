package com.unicauca.securityApp.porteria;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/porterias")
@AllArgsConstructor
public class PorteriaRestController {
    private final PorteriaService service;
     @PostMapping
    public Porteria save(@RequestBody Porteria porteria) {
        return service.save(porteria);
}
@GetMapping
    public Iterable<Porteria> findAll() {
        return service.findAll();
    }

}