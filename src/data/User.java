package data;

public class User {
	/** Username des Nutzers. */
	private final String name;
	
	/** Ctor des User.
	 * @param name Benutzername des Nutzers.
	 */
	public User(final String name) {
		this.name = name;
	}
	
	/** Getter fuer den Namen des Nutzers.
	 * @return Gibt den Namen des Nutzers zurueck.
	 */
	public String getName() {
		return name;
	}

	/** Gibt Informationen ueber die Klasse aus.
	 * @return Name des aktuellen Nutzers.
	 */
	@Override
	public String toString() {
		return name;
	}
}
