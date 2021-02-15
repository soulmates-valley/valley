module.exports = {
    replaceAt: function(index, character) {
        return substr(0, index) + character + substr(index+character.length);
    }
}