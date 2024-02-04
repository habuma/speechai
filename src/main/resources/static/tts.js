const TTS = {
    playAudio(text) {
        fetchAudio(text)
            .then((response) => {
                let blob = new Blob([response.value], { type: 'audio/mp3' });
                let url = window.URL.createObjectURL(blob)
                window.audio = new Audio();
                window.audio.src = url;
                window.audio.play();
            })
            .catch((error) => {});
    }
};

const fetchAudio = async (text) => {
    const requestOptions = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ "text": text })
    };
    let url = '/ask/tts';
    return fetch(url, requestOptions)
        .then(res => {
            if (!res.ok)
                throw new Error(`${res.status} = ${res.statusText}`);
            let reader = res.body.getReader();
            return reader
                .read()
                .then((result) => {
                    return result;
                });
        })
}