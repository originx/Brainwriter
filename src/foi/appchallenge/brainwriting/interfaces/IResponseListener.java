package foi.appchallenge.brainwriting.interfaces;
/**
 * Interface za uspje�nost obavljene zada�e
 * @author Mario Or�oli�
 * @version 1.0 - 16.01.2013.
 */
public interface IResponseListener {
	/**
	 * Succsessful response method handler
	 * @param data String of data returned
	 */
    void responseSuccess(String data);
    /**
     * Failed response method handler
     */
    void responseFail();
}
