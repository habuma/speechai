const Recorder = {

    audioType: null,
    mediaRecorder: null,
    onHearHandler: null,

    _startListening() {},
    _stopListening() {},
    startListening() { _startListening(); },
    stopListening() { _stopListening(); },

    onHear(heard) {console.log("Heard (Recorder): " + heard);},

    initMediaRecorder(onHearHandler) {
        this.onHearHandler = onHearHandler ? onHearHandler : this.onHear;
        this.audioType = this.supportedAudioTypes();
        navigator.mediaDevices.getUserMedia({ audio: true, video: false })
            .then(this.handleSuccess);
    },

    sendRecordingForTranscription(audioBlob) {
        const formData = new FormData();
        formData.append('audio', audioBlob, 'recording.' + this.audioType.split('/')[1]);
        fetch('/ask', {
            method: 'POST',
            body: formData
        })
            .then(async (response) => {
                const answer = await response.json();
                this.onHearHandler(answer.input, answer.response, answer.ttsBase64);
            })
            .catch((err) => {
                console.error(err);
            });
    },

    handleSuccess(stream) {
        const options = {mimeType: this.audioType};
        let recordedChunks = [];
        this.mediaRecorder = new MediaRecorder(stream, options);
        this.mediaRecorder.addEventListener('dataavailable', function(e) {
            if (e.data.size > 0) recordedChunks.push(e.data);
        });

        this.mediaRecorder.addEventListener('stop', function() {
            Recorder.sendRecordingForTranscription(new Blob(recordedChunks));
            recordedChunks = [];
        });

        this._startListening = () => { this.mediaRecorder.start(); };
        this._stopListening = () => { this.mediaRecorder.stop(); };
    },

    supportedAudioTypes() {
        const containers = ['webm', 'ogg', 'mp4', 'x-matroska', '3gpp', '3gpp2',
            '3gp2', 'quicktime', 'mpeg', 'aac', 'flac', 'wav']
        const codecs = ['vp9', 'vp8', 'avc1', 'av1', 'h265', 'h.265', 'h264',
            'h.264', 'opus', 'pcm', 'aac', 'mpeg', 'mp4a'];

        const supportedAudios = containers.map(format => `audio/${format}`)
            .filter(mimeType => MediaRecorder.isTypeSupported(mimeType))
        const supportedAudioCodecs = supportedAudios.flatMap(audio =>
            codecs.map(codec => `${audio};codecs=${codec}`))
            .filter(mimeType => MediaRecorder.isTypeSupported(mimeType))

        console.log('Supported Audio formats:', supportedAudios)
        console.log('Supported Audio codecs:', supportedAudioCodecs)
        return supportedAudios[0];
    }

};

