package es.codeurjc.webapp14.service;

import es.codeurjc.webapp14.model.Size;
import es.codeurjc.webapp14.repository.SizeRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeService {
    private final SizeRepository sizeRepository;

    public SizeService(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }

    public List<Size> getAllSizes() {
        return sizeRepository.findAll();
    }

    public Size getSizeById(Long id) {
        return sizeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Size not found"));
    }

    public Size saveSize(Size size) {
        return sizeRepository.save(size);
    }

    public void deleteSize(Long id) {
        sizeRepository.deleteById(id);
    }
}
