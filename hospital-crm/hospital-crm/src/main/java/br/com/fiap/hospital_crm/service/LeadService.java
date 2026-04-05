package br.com.fiap.hospital_crm.service;

import br.com.fiap.hospital_crm.exception.DuplicidadeException;
import br.com.fiap.hospital_crm.exception.EntidadeNaoEncontradaException;
import br.com.fiap.hospital_crm.model.Lead;
import br.com.fiap.hospital_crm.repository.LeadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsável pelas regras de negócio relacionadas a Leads.
 * Implementa validações de campos obrigatórios e verificação de duplicidade.
 */
@Service
public class LeadService {

    private final LeadRepository leadRepository;

    public LeadService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    /**
     * Regra de Negócio 1: Cadastrar Lead com validação de duplicidade.
     * - Valida campos obrigatórios (nome, telefone, canal de entrada).
     * - Verifica se já existe lead com o mesmo e-mail ou telefone.
     * - Lança DuplicidadeException se houver conflito.
     *
     * @param lead Lead a ser cadastrado
     * @return Lead salvo
     * @throws DuplicidadeException se já existir lead com mesmo email ou telefone
     * @throws IllegalArgumentException se campos obrigatórios não forem preenchidos
     */
    public Lead cadastrarLead(Lead lead) {
        // Validação de campos obrigatórios
        if (lead.getNome() == null || lead.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do lead é obrigatório.");
        }
        if (lead.getTelefone() == null || lead.getTelefone().isBlank()) {
            throw new IllegalArgumentException("O telefone do lead é obrigatório.");
        }
        if (lead.getCanalEntrada() == null || lead.getCanalEntrada().isBlank()) {
            throw new IllegalArgumentException("O canal de entrada do lead é obrigatório.");
        }

        // Verificação de duplicidade por e-mail
        if (lead.getEmail() != null && !lead.getEmail().isBlank()) {
            if (leadRepository.existePorEmail(lead.getEmail())) {
                throw new DuplicidadeException("Lead", "e-mail", lead.getEmail());
            }
        }

        // Verificação de duplicidade por telefone
        if (leadRepository.existePorTelefone(lead.getTelefone())) {
            throw new DuplicidadeException("Lead", "telefone", lead.getTelefone());
        }

        return leadRepository.salvar(lead);
    }

    /**
     * Busca um lead pelo ID.
     *
     * @param id ID do lead
     * @return Lead encontrado
     * @throws EntidadeNaoEncontradaException se não encontrar
     */
    public Lead buscarPorId(Long id) {
        return leadRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Lead", id));
    }

    /**
     * Lista todos os leads cadastrados.
     *
     * @return Lista de leads
     */
    public List<Lead> listarTodos() {
        return leadRepository.listarTodos();
    }
}
