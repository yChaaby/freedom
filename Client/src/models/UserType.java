/**
 * L'énumération UserType définit les différents types d'utilisateurs dans le système.
 * Chaque type d'utilisateur a un rôle spécifique dans l'interaction avec les sujets et les opinions.
 *
 * Types d'utilisateurs:
 * - REGULAR_USER: Utilisateur régulier sans rôle particulier.
 * - INFLUENCER: Utilisateur qui influence les opinions des autres.
 * - CRITICAL_THINKER: Utilisateur qui analyse de manière critique les opinions avant de les accepter.
 * - PROPOSER: Utilisateur qui propose de nouveaux sujets de discussion.
 * - CONSENSUS_FINDER: Utilisateur qui cherche à trouver un consensus parmi les opinions divergentes.
 */
package models;

public enum UserType {
    REGULAR_USER,
    INFLUENCER,
    CRITICAL_THINKER,
    PROPOSER,
    CONSENSUS_FINDER,
}