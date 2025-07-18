package br.com.bancodigital.api.service;

import br.com.bancodigital.api.model.entity.Cliente;
import br.com.bancodigital.api.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente salvarCliente(Cliente cliente) {

        if (clienteRepository.findByCpf(cliente.getCpf()).isPresent()){
            throw new IllegalArgumentException("Cliente com CPF " + cliente.getCpf() + " já cadastrado.");
        }

        if(Period.between(cliente.getDataNascimento(), LocalDate.now()).getYears() < 18) {
            throw new IllegalArgumentException("Cliente deve ser maior de 18 anos.");
        }

        String senhaCodificada = passwordEncoder.encode(cliente.getSenha());
        cliente.setSenha(senhaCodificada);

        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente atualizarCliente(Long id, Cliente dadosCliente){
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente com ID " + id + " não encontrado."));

        clienteExistente.setNome(dadosCliente.getNome());
        clienteExistente.setDataNascimento(dadosCliente.getDataNascimento());
        clienteExistente.setEndereco(dadosCliente.getEndereco());
        clienteExistente.setCategoria(dadosCliente.getCategoria());

        return clienteRepository.save(clienteExistente);
    }

    public void deletarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente com ID " + id + " não encontrado.");
        }

        clienteRepository.deleteById(id);
    }

}
