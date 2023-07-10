
function deleteBy(id) {
    const url = '/odontologos/' + id;
    const settings = {
        method: 'DELETE'
    };

    fetch(url, settings)
        .then(response => {
            if (response.ok) {
                let row_id = "#tr_" + id;
                document.querySelector(row_id).remove();

                let successAlert = '<div class="alert alert-success alert-dismissible">' +
                    '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
                    '<strong>Odontólogo eliminado</strong></div>';

                document.querySelector('#response').innerHTML = successAlert;
                document.querySelector('#response').style.display = "block";
                document.querySelector('#div_odontologo_updating').style.display = "none";
            } else {
                              return response.json().then(errorData => {
                                  throw new Error(errorData.message);
                              });
                          }
                      })
        .catch(error => {
            console.error(error);
            // Mostrar mensaje de error con el mensaje devuelto por el servidor
            let errorAlert = '<div class="alert alert-danger alert-dismissible">' +
                '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
                '<strong>Error:</strong> ' + error.message + '</div>';

            document.querySelector('#response').innerHTML = errorAlert;
            document.querySelector('#response').style.display = "block";
            document.querySelector('#div_odontologo_updating').style.display = "none";
        });
}


