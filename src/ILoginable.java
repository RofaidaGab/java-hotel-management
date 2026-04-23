public interface ILoginable {
    boolean login(String username, String password);

    void logout();

}