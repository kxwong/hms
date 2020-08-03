package Model;

import javax.annotation.Resource;
import javax.persistence.*;

public class AccountManager {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    Query query;

    public AccountManager(EntityManager mgr) {
        this.mgr = mgr;
    }

    public boolean addAccount(Account account) {
        mgr.persist(account);
        return true;
    }

    public Account findAccountByID(String id) {
        Account account = mgr.find(Account.class, id);
        return account;
    }

    public Account findAccountByUsername(String username){
        Account account = mgr.find(Account.class, username);
        return account;
    }
    
    public boolean updateAccount(Account account) throws Exception {
        Account tempoAccount = findAccountByID(account.getUsername());
        if (tempoAccount != null) {
            tempoAccount.setPassword(account.getPassword());
            tempoAccount.setLevel(account.getLevel());
            tempoAccount.setHosteller(account.getHosteller());
            return true;
        }
        return false;
    }

    public Account findAccount(String username, String password) {
        Account account = (Account) mgr.createNamedQuery("Account.accountAuthenticate").setParameter("username", username).setParameter("password", password).getSingleResult();
        int level = accountAuthenticate(username, password);
        if (level == 2 || level == 3) {
            return account;
        } else {
            return null;
        }
    }

    public boolean deleteAccount(String id) {
        Account account = findAccountByID(id);
        if (account != null) {
            mgr.remove(account);
            return true;
        }
        return false;
    }

    public boolean updateAccountPassword(Account account) throws Exception {
        Account tempoAccount = findAccountByID(account.getUsername());
        if (tempoAccount != null) {
            tempoAccount.setPassword(account.getPassword());
        }
        return false;
    }

    public int accountAuthenticate(String username, String password) {
        int result;
        try {
            Account account = (Account) mgr.createNamedQuery("Account.accountAuthenticate").setParameter("username", username).setParameter("password", password).getSingleResult();
            result = account.getLevel();
        } catch (Exception ex) {
            result = 0;
        }
        return result;
    }
}
