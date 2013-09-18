package foi.appchallenge.brainwriting.modules;

import java.util.List;

import org.apache.http.NameValuePair;

/**
 * Get parametrs
 * @author Mario Oršoliæ
 *
 */
public final class GetParameters {
	/**
	 * Url which is sent in GET request
	 */
    public String url;
    /**
     * List of NameValuePair variables
     */
    public List<NameValuePair> nameValuePairs;
}