package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public boolean addCredential(Credential credential) {
        return credentialMapper.insert(credential) >= 0;
    }

    public boolean credentialExists(int credentialId) {
        return credentialMapper.getCredential(credentialId) != null;
    }

    public List<Credential> getAllUserCredentials(int userId) {
        return credentialMapper.getAllUserCredentials(userId);
    }

    public void updateCredential(Credential credential){
        credentialMapper.updateCredential(credential);
    }

    public void deleteCredential(Integer id) {
        credentialMapper.delete(id);
    }
}
