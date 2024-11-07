window.onload = function() {

    const postContainer = document.querySelector('.card-con');
    
    // Create post element
    axios
    .get('/travel-book')
    .then(res => {
        console.log(res.data);
        createPostElement(res.data, postContainer);
    })
    .catch(err => {
        console.error(err);
    });

}

function createPostElement(data, postContainer) {
    /* debugger */;
    const existingIds = Array.from(postContainer.children)
        .map(child => child.dataset.id);

    const uniqueData = data.filter(d => !existingIds.includes(d.id));

    let c = '';
    data.forEach(d => {
        //<li class="card-img" style="background:url('/api/v1/board/file?filePath=${d.files[0].fileUrl}') no-repeat center/cover;}"></li>
        c += `
            <ul class="card-box" data-id="${d.link}" onclick = "window.open('${d.link}', '_blank')">
                <li class="card-detail">
                    <span class="title">${d.title}</span>
                    <span class="content">${d.description}</span>
                    <span class="date">${d.postdate}</span>
                </li>
            </ul>
        `;
    })
    postContainer.innerHTML = c;
}


