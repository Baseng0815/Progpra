/**
 * 
 */
package it_sec;

import com.macasaet.fernet.Validator;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.function.Function;

/**
 * Custom Fernet validator for byte arrays
 * 
 * @author elmar
 *
 */
public class BytesValidator implements Validator<byte[]> {

	/**
	 * Interpret bytes as bytes (no transformation)
	 */
	@Override
	public Function<byte[], byte[]> getTransformer() {
		return Function.identity();
	}

	/**
	 * Allow decryption any time after encryption.
	 * By default, Fernet limits this to 60s.
	 */
	@Override
	public TemporalAmount getTimeToLive() {
		return Duration.ofSeconds(Instant.MAX.getEpochSecond());
	}
}
