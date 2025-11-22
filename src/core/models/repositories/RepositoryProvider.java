/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.repositories;

public final class RepositoryProvider {

    private static IRepositoryProvider provider = new DefaultRepositoryProvider();

   
    public static IRepositoryProvider getProvider() {
        return provider;
    }
    
    public static void setProvider(IRepositoryProvider customProvider) {
        if (customProvider != null) {
            provider = customProvider;
        }
    }

    private static class DefaultRepositoryProvider implements IRepositoryProvider {
        private final IBookRepository bookRepo = new BookRepository();
        private final IPersonRepository personRepo = new PersonRepository();
        private final IPublisherRepository publisherRepo = new PublisherRepository();
        private final IStandRepository standRepo = new StandRepository();

        @Override
        public IBookRepository getBookRepository() {
            return bookRepo;
        }

        @Override
        public IPersonRepository getPersonRepository() {
            return personRepo;
        }

        @Override
        public IPublisherRepository getPublisherRepository() {
            return publisherRepo;
        }

        @Override
        public IStandRepository getStandRepository() {
            return standRepo;
        }
    }
}