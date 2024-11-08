package com.unicauca.securityApp.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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

        public void guardarUsuario(Usuario usuario){
            Usuario usuarioExistente = usuarioRepository.findByCedula(usuario.getCedula());
            if (usuarioExistente != null) {
                throw new RuntimeException("El usuario con cédula " + usuario.getCedula() + " ya existe");
            }

            List<Usuario> usuarios = usuarioRepository.findAll();

            Set<Integer> valoresOcupados = new HashSet<>();
            for (Usuario u : usuarios) {
                valoresOcupados.add(u.getIn());
            }

            int valorLibre = -1;
            for (int i = 1; i <= 120; i++) {
                if (!valoresOcupados.contains(i)) {
                    valorLibre = i;
                    break;
                }
            }

            if (valorLibre == -1) {
                throw new RuntimeException("No hay valores libres de `in` disponibles");
            }

            usuario.setIn(valorLibre);


            String cedula = usuario.getCedula();
            String url = microcontrollerUrl + "/receive";


            Map<String, Object> payload = new HashMap<>();
            payload.put("cedula", cedula);
            payload.put("valorLibre", valorLibre);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            try {
                ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    System.out.println("Solicitud POST enviada exitosamente al microcontrolador");
                    usuarioRepository.save(usuario);
                } else {
                    System.err.println("Error al enviar la solicitud POST al microcontrolador");

                }
            } catch (Exception e) {
                System.err.println("Error al conectar con el microcontrolador: " + e.getMessage());
            }



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
