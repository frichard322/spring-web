package edu.bbte.idde.frim1910.spring.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class ByteArrayMapper {
    public String byteArrayToString(byte[] input) {
        return input == null ? "" : new String(input);
    }

    public byte[] stringToByteArray(String input) {
        return input == null ? new byte[0] : input.getBytes();
    }
}
