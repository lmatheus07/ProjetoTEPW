package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.config.MinioProperties;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    // Garante que o bucket existe ao iniciar — chamado pelo LivroService
    public void garantirBucket() {
        try {
            boolean existe = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .build()
            );
            if (!existe) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(minioProperties.getBucketName())
                                .build()
                );
            }
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao verificar/criar bucket no MinIO: " + ex.getMessage());
        }
    }

    // Faz o upload do PDF e retorna o nome do arquivo salvo
    public String uploadPdf(MultipartFile arquivo) {
        validarPdf(arquivo);
        garantirBucket();

        // Gera nome único para evitar conflito entre arquivos com mesmo nome
        String nomeArquivo = UUID.randomUUID() + "_" + arquivo.getOriginalFilename();

        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(nomeArquivo)
                            .stream(arquivo.getInputStream(), arquivo.getSize(), -1)
                            .contentType("application/pdf")
                            .build()
            );
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao fazer upload do PDF: " + ex.getMessage());
        }

        return nomeArquivo;
    }

    // Gera URL temporária para download (válida por 1 hora)
    public String gerarUrlDownload(String nomeArquivo) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(nomeArquivo)
                            .method(Method.GET)
                            .expiry(1, TimeUnit.HOURS) // link expira em 1 hora
                            .build()
            );
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao gerar URL de download: " + ex.getMessage());
        }
    }

    // Remove o PDF do MinIO ao excluir um livro
    public void deletarPdf(String nomeArquivo) {
        if (nomeArquivo == null || nomeArquivo.isBlank()) return;
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(nomeArquivo)
                            .build()
            );
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao deletar PDF: " + ex.getMessage());
        }
    }

    // Valida se o arquivo enviado é realmente um PDF
    private void validarPdf(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("Arquivo PDF não pode ser vazio.");
        }
        String contentType = arquivo.getContentType();
        if (!"application/pdf".equals(contentType)) {
            throw new IllegalArgumentException(
                    "Tipo de arquivo inválido: " + contentType + ". Apenas PDFs são aceitos.");
        }
        // Limite de 50MB
        long limiteBytes = 50L * 1024 * 1024;
        if (arquivo.getSize() > limiteBytes) {
            throw new IllegalArgumentException("Arquivo excede o limite de 50MB.");
        }
    }
}