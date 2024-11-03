package com.unicauca.securityApp;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service

public class UsuarioService {

        private final UsuarioRepository usuarioRepository;

        // Inyectar la URL desde el archivo de propiedades
        @Value("${microcontroller.url}")
        private String microcontrollerUrl;

        // Inyectar RestTemplate
        private final RestTemplate restTemplate;

        @Autowired
        public UsuarioService(UsuarioRepository usuarioRepository, RestTemplate restTemplate) {
                this.usuarioRepository = usuarioRepository;
                this.restTemplate = restTemplate;
        }

        public void guardarUsuario(Usuario usuario) {

                Usuario usuarioExistente = usuarioRepository.findByCedula(usuario.getCedula());
                if (usuarioExistente != null) {
                        throw new RuntimeException("El usuario con cédula " + usuario.getCedula() + " ya existe");
                }
            usuarioRepository.save(usuario);
     String cedula = usuario.getCedula();

        String url = microcontrollerUrl + "/receive";
/*
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, cedula, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Solicitud POST enviada exitosamente al microcontrolador");
            } else {
                System.err.println("Error al enviar la solicitud POST al microcontrolador");
            }

        } catch (Exception e) {
            System.err.println("Error al conectar con el microcontrolador: " + e.getMessage());
        }
*/

        }
        public Page<Usuario> obtenerUsuarios(int page, int size) {
                return usuarioRepository.findAll(PageRequest.of(page, size));
        }

        public void eliminarUsuario(String cedula) {
                Usuario usuario = usuarioRepository.findByCedula(cedula);
                if (usuario == null) {
                        throw new RuntimeException("El usuario con cédula " + cedula + " no existe");
                }
                usuarioRepository.delete(usuario);
        }
        public void actualizarUsuario(Usuario usuario) {
                Usuario usuarioExistente = usuarioRepository.findByCedula(usuario.getCedula());
                if (usuarioExistente == null) {
                        throw new RuntimeException("El usuario con cédula " + usuario.getCedula() + " no existe");
                }
                usuarioRepository.save(usuario);

        }


}
