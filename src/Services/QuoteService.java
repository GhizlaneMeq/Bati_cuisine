package Services;

import Repositories.QuoteRepository;

public class QuoteService {
    private QuoteRepository quoteRepository;

    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }
}
