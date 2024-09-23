package Services;

import Entities.Quote;
import Repositories.QuoteRepository;

import java.util.List;
import java.util.Optional;

public class QuoteService {
    private final QuoteRepository quoteRepository;

    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public Quote save(Quote quote) {
        return quoteRepository.save(quote);
    }

    public Optional<Quote> findById(Long id) {
        return quoteRepository.findById(id);
    }

    public List<Quote> findAll() {
        return quoteRepository.findAll();
    }

    public Quote update(Quote quote) {
        return quoteRepository.update(quote);
    }

    public boolean delete(Long id) {
        return quoteRepository.delete(id);
    }
}
