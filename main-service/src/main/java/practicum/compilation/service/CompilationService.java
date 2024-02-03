package practicum.compilation.service;

import org.springframework.stereotype.Service;
import practicum.compilation.dto.NewCompilationDto;
import practicum.compilation.dto.UpdateCompilationRequest;
import practicum.compilation.model.Compilation;

import java.util.List;

@Service
public interface CompilationService {
    Compilation add(NewCompilationDto compilationDto);

    void delete(Long compilationId);

    Compilation get(Long compId);

    List<Compilation> getAll(Boolean pinned, Integer from, Integer size);

    Compilation update(Long compId, UpdateCompilationRequest compilationRequest);
}
