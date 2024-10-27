package com.unicauca.securityApp.porteria;

import org.springframework.stereotype.Service;

@Service
public class PorteriaService {
    private final PorteriaRepository porterias;

    public PorteriaService(PorteriaRepository porterias) {
        this.porterias = porterias;
    }

    public Porteria save(Porteria porteria) {
        return porterias.save(porteria);
    }

    public Porteria findById(String id) {
        return porterias.findById(id).orElse(null);
    }

    public Iterable<Porteria> findAll() {
        return porterias.findAll();
    }

    public void deleteById(String id) {
        porterias.deleteById(id);
    }
}
