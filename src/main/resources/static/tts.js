const TTS = {
    async playAudio(base64Bytes) {
        let base64Url = "data:audio/mp3;base64," + base64Bytes;
        window.audio = new Audio();
        window.audio.src = base64Url;
        await window.audio.play();
    }
};
