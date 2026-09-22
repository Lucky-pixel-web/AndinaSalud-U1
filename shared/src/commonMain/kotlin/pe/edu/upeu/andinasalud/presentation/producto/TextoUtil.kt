package pe.edu.upeu.andinasalud.presentation

/**
 * Normaliza texto para comparaciones de búsqueda: minúsculas y sin tildes.
 * commonMain no tiene java.text.Normalizer, así que se hace el reemplazo manual.
 */
fun String.normalizarParaBusqueda(): String {
    val conMinusculas = this.lowercase()
    val mapaAcentos = mapOf(
        'á' to 'a', 'é' to 'e', 'í' to 'i', 'ó' to 'o', 'ú' to 'u', 'ñ' to 'n'
    )
    return conMinusculas.map { mapaAcentos[it] ?: it }.joinToString("")
}