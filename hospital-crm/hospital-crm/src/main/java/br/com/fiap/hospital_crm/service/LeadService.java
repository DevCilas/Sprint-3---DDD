package br.com.fiap.hospital_crm.service;

import br.com.fiap.hospital_crm.exception.DuplicidadeException;
import br.com.fiap.hospital_crm.exception.EntidadeNaoEncontradaException;
import br.com.fiap.hospital_crm.model.Lead;
import br.com.fiap.hospital_crm.repository.LeadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeadService {

    private final LeadRepository leadRepository;

    public LeadService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    // cadastra o lead verificando se ja nao existe no banco (email ou telefone)
    public Lead cadastrarLead(Lead lead) {
        if (lead.getNome() == null || lead.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do lead é obrigatório.");
        }
        if (lead.getTelefone() == null || lead.getTelefone().isBlank()) {
            throw new IllegalArgumentException("O telefone do lead é obrigatório.");
        }
        if (lead.getCanalEntrada() == null || lead.getCanalEntrada().isBlank()) {
            throw new IllegalArgumentException("O canal de entrada do lead é obrigatório.");
        }

        // checa duplicidade por email
        if (lead.getEmail() != null && !lead.getEmail().isBlank()) {
            if (leadRepository.existePorEmail(lead.getEmail())) {
                throw new DuplicidadeException("Lead", "e-mail", lead.getEmail());
            }
        }

        // checa duplicidade por telefone
        if (leadRepository.existePorTelefone(lead.getTelefone())) {
            throw new DuplicidadeException("Lead", "telefone", lead.getTelefone());
        }

        return leadRepository.salvar(lead);
    }

    public Lead buscarPorId(Long id) {
        return leadRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Lead", id));
    }

    public List<Lead> listarTodos() {
        return leadRepository.listarTodos();
    }
}
