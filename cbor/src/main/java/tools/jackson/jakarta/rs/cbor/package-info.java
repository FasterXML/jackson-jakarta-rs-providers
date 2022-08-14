/**
 * Jackson-based Jakarta-RS provider that can automatically
 * serialize and deserialize resources for 
 * Compact Binary Object Representation (CBOR) content type (MediaType).
 *<p>
 * Also continues supporting functionality, such as
 * exception mappers that can simplify handling of
 * error conditions.
 *<p>
 * There are two default provider classes:
 *<ul>
 * <li>{@link tools.jackson.jakarta.rs.cbor.JacksonCBORProvider} is the basic
 *    provider configured to use Jackson annotations
 *  </li>
 * <li>{@link tools.jackson.jakarta.rs.cbor.JacksonXmlBindCBORProvider} is extension
 *    of the basic provider, configured to additionally use Jakarta XmlBind annotations,
 *    in addition to (or in addition of, if so configured) Jackson annotations.
 *  </li>
 * </ul>
 */
package tools.jackson.jakarta.rs.cbor;
